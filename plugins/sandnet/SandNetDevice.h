/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * SandNetDevice.h
 * Interface for the sandnet device
 * Copyright (C) 2005-2009 Simon Newton
 */

#ifndef PLUGINS_SANDNET_SANDNETDEVICE_H_
#define PLUGINS_SANDNET_SANDNETDEVICE_H_

#include <string>
#include "olad/Device.h"
#include "olad/Plugin.h"
#include "olad/PluginAdaptor.h"
#include "plugins/sandnet/SandNetCommon.h"
#include "plugins/sandnet/SandNetNode.h"

namespace ola {
namespace plugin {
namespace sandnet {

class SandNetDevice: public ola::Device {
  public:
    SandNetDevice(class SandNetPlugin *owner,
                  const std::string &name,
                  class Preferences *prefs,
                  const class PluginAdaptor *plugin_adaptor);
    ~SandNetDevice() {}

    bool Start();
    bool Stop();
    string DeviceId() const { return "1"; }
    SandNetNode *GetNode() { return m_node; }

    int SendAdvertisement();

    static const char NAME_KEY[];

  private:
    class Preferences *m_preferences;
    const class PluginAdaptor *m_plugin_adaptor;
    SandNetNode *m_node;
    bool m_enabled;
    ola::network::timeout_id m_timeout_id;

    static const char IP_KEY[];
    static const int INPUT_PORTS = 8;  // the number of input ports to create
    // send an advertistment every 2s.
    static const int ADVERTISTMENT_PERIOD_MS = 2000;
};
}  // sandnet
}  // plugin
}  // ola
#endif  // PLUGINS_SANDNET_SANDNETDEVICE_H_
